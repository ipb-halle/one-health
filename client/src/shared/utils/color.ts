export function getRandomColor() {
    // Generate random values for red, green, and blue components
    const r = Math.floor(Math.random() * 256); // Random integer between 0 and 255
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);

    // Convert RGB values to hexadecimal format
    const hexR = r.toString(16).padStart(2, '0'); // Convert to hexadecimal and ensure two digits
    const hexG = g.toString(16).padStart(2, '0');
    const hexB = b.toString(16).padStart(2, '0');

    // Concatenate hexadecimal values to form the color string
    const hexColor = `#${hexR}${hexG}${hexB}`;

    return hexColor;
}

export function darkenHexColor(hex: string, percent: number) {
    // Remove '#' if present
    hex = hex.replace(/^#/, '');

    // Convert hex to RGB
    let r = parseInt(hex.substring(0, 2), 16);
    let g = parseInt(hex.substring(2, 4), 16);
    let b = parseInt(hex.substring(4, 6), 16);

    // Darken the color
    r = Math.floor((r * (100 - percent)) / 100);
    g = Math.floor((g * (100 - percent)) / 100);
    b = Math.floor((b * (100 - percent)) / 100);

    // Ensure that values are within valid range (0-255)
    r = r < 0 ? 0 : r > 255 ? 255 : r;
    g = g < 0 ? 0 : g > 255 ? 255 : g;
    b = b < 0 ? 0 : b > 255 ? 255 : b;

    // Convert RGB back to hex
    const darkerHex =
        '#' + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);

    return darkerHex;
}

export function getContrastColor(backgroundColor: string) {
    // Convert the hexadecimal color to RGB components
    const r = parseInt(backgroundColor.substr(1, 2), 16);
    const g = parseInt(backgroundColor.substr(3, 2), 16);
    const b = parseInt(backgroundColor.substr(5, 2), 16);

    // Calculate the luminance (perceived brightness) using the formula
    const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

    // Choose black or white based on luminance
    return luminance > 0.7 ? '#000000' : '#ffffff';
}
